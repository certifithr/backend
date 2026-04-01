package org.certifit.application.exercise;

import jakarta.persistence.criteria.Predicate;
import org.certifit.db.entity.ExerciseEntity;
import org.springframework.data.jpa.domain.Specification;

import org.certifit.application.exercise.dto.ExerciseFilterDto;
import java.util.ArrayList;
import java.util.List;

public class ExerciseSpecification {

    private ExerciseSpecification() {}

    public static Specification<ExerciseEntity> withFilters(ExerciseFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasValue(filter.name())) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.name().toLowerCase() + "%"
                ));
            }

            if (hasValue(filter.category())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("category")),
                        filter.category().toLowerCase()
                ));
            }

            if (hasValue(filter.difficulty())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("difficulty")),
                        filter.difficulty().toLowerCase()
                ));
            }

            if (hasValue(filter.force())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("force")),
                        filter.force().toLowerCase()
                ));
            }

            if (hasValue(filter.mechanic())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("mechanic")),
                        filter.mechanic().toLowerCase()
                ));
            }

            // jsonb muscle search — uses native PostgreSQL @> operator via function
            if (hasValue(filter.muscle())) {
                predicates.add(cb.isTrue(
                        cb.function(
                                "jsonb_contains_text",
                                Boolean.class,
                                root.get("musclesPrimary"),
                                cb.literal(filter.muscle())
                        )
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}