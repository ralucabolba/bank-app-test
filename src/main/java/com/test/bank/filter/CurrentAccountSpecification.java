package com.test.bank.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.test.bank.model.CurrentAccount;

public class CurrentAccountSpecification implements Specification<CurrentAccount> {

	private static final long serialVersionUID = -1832936039756532080L;

	private List<SearchCriteria> searchCriterias;

	public CurrentAccountSpecification(List<SearchCriteria> searchCriterias) {
		this.searchCriterias = searchCriterias;
	}

	public void add(SearchCriteria searchCriteria) {
		this.searchCriterias.add(searchCriteria);
	}

	@Override
	public Predicate toPredicate(Root<CurrentAccount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<>();

		for (SearchCriteria criteria : searchCriterias) {
			if (criteria.getValue() instanceof LocalDateTime) {
				predicates.add(create(root, criteria, builder));
				continue;
			}
			if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
				predicates.add(builder.greaterThan(root.get(criteria.getName()), criteria.getValue().toString()));
			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
				predicates.add(builder.lessThan(root.get(criteria.getName()), criteria.getValue().toString()));
			} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUALS)) {
				predicates.add(builder.notEqual(root.get(criteria.getName()), criteria.getValue()));
			} else if (criteria.getOperation().equals(SearchOperation.EQUALS)) {
				if (criteria.getValue() instanceof String) {
					// for string values it acts as a LIKE operator
					predicates.add(
							builder.like(root.get(criteria.getName()), "%" + criteria.getValue().toString() + "%"));
				} else {
					predicates.add(builder.equal(root.get(criteria.getName()), criteria.getValue()));
				}
			}
		}

		return builder.and(predicates.toArray(new Predicate[0]));
	}

	public Predicate create(Root<CurrentAccount> root, SearchCriteria searchCriteria, CriteriaBuilder cb) {
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
