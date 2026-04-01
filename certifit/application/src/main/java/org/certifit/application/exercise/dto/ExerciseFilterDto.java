package org.certifit.application.exercise.dto;

public record ExerciseFilterDto(
        String name,
        String category,
        String difficulty,
        String force,
        String mechanic,
        String muscle
) {
    @Override
    public String toString() {
        return "name=" + name + ", category=" + category +
                ", difficulty=" + difficulty + ", force=" + force +
                ", mechanic=" + mechanic + ", muscle=" + muscle;
    }
}