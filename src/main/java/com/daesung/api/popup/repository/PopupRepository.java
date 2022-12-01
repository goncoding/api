package com.daesung.api.popup.repository;

import com.daesung.api.popup.domain.Popup;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup, Long> , PopupRepositoryCustom{

    Page<Popup> searchPopupList(Search search, Pageable pageable);
}
