package com.ssp.platform.entity;

import com.ssp.platform.entity.enums.SupplyStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "supply", schema = "public", catalog = "experimental")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class SupplyEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @NonNull
    @Column(name = "purchase_id")
    private UUID purchaseId;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "create_date")
    private Date createDate;

    //@ManyToOne
    //@JoinColumn(name = "author_id", nullable = false)
    //private User authorId;

    @NonNull
    @Column(name = "budget")
    private long budget;

    @NonNull
    @Column(name = "requirements")
    private String requirements;

    @NonNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SupplyStatus status;

    @NonNull
    @Column(name = "result_of_consideration")
    private String resultOfConsideration;

    @NonNull
    @Column(name = "result_date")
    private Timestamp resultDate;

    //@OneToOne(fetch = FetchType.LAZY, mappedBy = "supplyId")
    //@Column(name = "file_id")
    //private FileEntity file;
}
