package com.daesung.api.common.domain.enumType;

import com.daesung.api.utils.date.RegTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "ds_editor_file")
public class EditorFile extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edit_file_id")
    private Long id;

    @Column(length = 512)
    private String editFileOriginalName;
    @Column(length = 512)
    private String editFileSavedName;
    @Column(length = 1024)
    private String editFileSavedPath;

    private String regUser;






}
