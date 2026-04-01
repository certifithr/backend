package org.certifit.application.exercise;

import org.certifit.application.exercise.dto.ExerciseFilterDto;
import org.certifit.application.scraper.dto.ExerciseDto;
import org.certifit.application.scraper.mapper.ExerciseMapper;
import org.certifit.db.entity.ExerciseEntity;
import org.certifit.db.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExerciseService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseService.class);

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    public Page<ExerciseDto> findAll(ExerciseFilterDto filter, Pageable pageable) {
        Pageable p = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));

        Specification<ExerciseEntity> spec = ExerciseSpecification.withFilters(filter);

        Page<ExerciseDto> result = exerciseRepository.findAll(spec, p).map(exerciseMapper::fromEntity);
        log.info("findAll — filters: {}, page: {}, total: {}", filter, p.getPageNumber(), result.getTotalElements());
        return result;
    }

    public Optional<ExerciseDto> findById(Integer id) {
        return exerciseRepository.findById(id).map(exerciseMapper::fromEntity);
    }
}