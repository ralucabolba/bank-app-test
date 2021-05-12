package com.test.bank.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.test.bank.model.Transaction;

public class TransactionSpecification implements Specification<Transaction> {

	private static final long serialVersionUID = -1832936039756532080L;

	private List<SearchCriteria> searchCriterias;

	public TransactionSpecification(List<SearchCriteria> searchCriterias) {
		this.searchCriterias = searchCriterias;
	}

	public void add(SearchCriteria searchCriteria) {
		this.searchCriterias.add(searchCriteria);
	}

	@Override
	public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<>();
		
		for (SearchCriteria criteria : searchCriterias) {
			From<?, ?> usedRoot = root;
			String name = criteria.getName();
			
			// allows filtering by join tables
			if (criteria.getName().contains(".")) {
				// join table
				String[] names = name.split("\\.");
				usedRoot = getOrCreateJoin(usedRoot, names[0], JoinType.INNER);
				name = names[1];
			}
			if (criteria.getValue() instanceof LocalDateTime) {
				predicates.add(createDateTimePredicate(usedRoot, criteria, builder));
			} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
				predicates.add(builder.greaterThan(usedRoot.get(name), criteria.getValue().toString()));
			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
				predicates.add(builder.lessThan(usedRoot.get(name), criteria.getValue().toString()));
			} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUALS)) {
				predicates.add(builder.notEqual(usedRoot.get(name), criteria.getValue()));
			} else if (criteria.getOperation().equals(SearchOperation.EQUALS)) {
				if (criteria.getValue() instanceof String) {
					// for string values it acts as a LIKE operator
					predicates.add(
							builder.like(usedRoot.get(name), "%" + criteria.getValue().toString() + "%"));
				} else {
					predicates.add(builder.equal(usedRoot.get(name), criteria.getValue()));
				}
			}
		}

		return builder.and(predicates.toArray(new Predicate[0]));
	}
	
	private static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute, JoinType joinType) {
	    for (Join<?, ?> join : from.getJoins()) {
	        boolean sameName = join.getAttribute().getName().equals(attribute);
	        if (sameName && join.getJoinType().equals(joinType)) {
	            return join;
	        }
	    }
	    return from.join(attribute, joinType);
	}

	public Predicate createDateTimePredicate(From<?, ?> root, SearchCriteria searchCriteria,
			CriteriaBuilder cb) {
		LocalDate startDate = LocalDate.of(1000, 1, 1);
		Integer startHour = 0;
		Integer startMin = 0;
		LocalDate endDate = LocalDate.of(9999, 1, 1);
		Integer endHour = 23;
		Integer endMin = 59;

		LocalDateTime value = (LocalDateTime) searchCriteria.getValue();

		if (searchCriteria.getOperation().equals(SearchOperation.GREATER_THAN)
				|| searchCriteria.getOperation().equals(SearchOperation.EQUALS)) {
			startHour = value.getHour();
			startMin = value.getMinute();
			startDate = LocalDate.of(value.getYear(), value.getMonth(), value.getDayOfMonth());
		}

		if (searchCriteria.getOperation().equals(SearchOperation.LESS_THAN)
				|| searchCriteria.getOperation().equals(SearchOperation.EQUALS)) {
			endHour = value.getHour();
			endMin = value.getMinute();
			endDate = LocalDate.of(value.getYear(), value.getMonth(), value.getDayOfMonth());
		}

		return cb.between(root.get(searchCriteria.getName()), startDate.atTime(startHour, startMin, 0),
				endDate.atTime(endHour, endMin, 59));
	}

}
