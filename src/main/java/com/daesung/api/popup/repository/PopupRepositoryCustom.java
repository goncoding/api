package com.daesung.api.popup.repository;

import com.daesung.api.popup.domain.Popup;
import com.daesung.api.utils.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PopupRepositoryCustom{

    Page<Popup> searchPopupList(Search search, Pageable pageable);

}
