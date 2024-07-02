package app.root.repository;

import app.root.model.Obec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository
 */
@Repository
public interface ObecRepository extends JpaRepository <Obec, Long> {
}
