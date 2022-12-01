package com.daesung.api.common.repository;

import com.daesung.api.common.domain.enumType.EditorFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorFileRepository extends JpaRepository<EditorFile, Long> {
}
