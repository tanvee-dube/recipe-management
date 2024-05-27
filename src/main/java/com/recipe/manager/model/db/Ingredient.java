package com.recipe.manager.model.db;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(fluent = true)
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "recipeIngredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;
    
    @NotBlank
    @Column(nullable = false)
    private String description;
    

    @ManyToMany(mappedBy = "recipeIngredients", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties("recipeIngredients")
    private Set<Recipe> recipeIngredients;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
