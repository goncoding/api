package com.daesung.api.accounts.domain;


import com.daesung.api.accounts.domain.enumType.AccountRole;
import com.daesung.api.utils.date.BaseTimeEntity;
import com.daesung.api.utils.date.RegTimeEntity;
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
@Table(name = "ds_account")
public class Account extends RegTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ac_id")
    private Long id;

    @Column(unique = true)
    private String loginId;
    private String loginPwd;
    private String acName; //계정명
    private String acEmail; //공용이메일

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="ds_account_roles", joinColumns = @JoinColumn(name= "ac_id"))
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles; //사원권한

}
