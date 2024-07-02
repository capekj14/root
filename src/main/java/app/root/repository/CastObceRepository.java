package app.root.repository;

import app.root.model.CastObce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * basic JPA repository
 */
@Repository
public interface CastObceRepository extends JpaRepository <CastObce, Long> {
}
