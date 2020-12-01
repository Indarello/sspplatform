package com.ssp.platform.entity;

import com.ssp.platform.entity.enums.SupplyStatus;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "supply")
public class SupplyEntity {

    public static final long DATE_DIVIDER = 1000L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name = "purchase_id")
    private UUID purchaseId;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private Long createDate;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "author")
    @NotFound(action = NotFoundAction.IGNORE)
    private User author;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "comment")
    private String comment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SupplyStatus status;

    @Column(name = "result_of_consideration")
    private String resultOfConsideration;

    @Column(name = "result_date")
    private Long resultDate;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = FileEntity.class)
    @JoinColumn(name = "file")
    @NotFound(action = NotFoundAction.IGNORE)
    private FileEntity file;
    
    public SupplyEntity(){
        createDate = System.currentTimeMillis() / DATE_DIVIDER;
    }

    public SupplyEntity(
            UUID purchaseId, String description, User author,
            Long budget, String comment, FileEntity file
    ) {
        this.purchaseId = purchaseId;
        this.description = description;
        this.author = author;
        this.budget = budget;
        this.comment = comment;
        this.file = file;

        createDate = System.currentTimeMillis() / DATE_DIVIDER;
        status = SupplyStatus.CONSIDERED;
    }
}
