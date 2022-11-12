package com.daesung.api.accounts.domain;


import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "ds_admin_user")
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ac_id")
    private Long id;

    @Column(unique = true)
    private String loginId;
    private String loginPwd;
    private String acNum; //사원번호
    private String acName; //사원명

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="ds_admin_roles", joinColumns = @JoinColumn(name= "ac_id"))
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles; //사원권한



}
