package com.ssp.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssp.platform.entity.enums.PurchaseStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.*;

@Entity
@Table(name = "purchases")
@Data
public class Purchase
{
    /**
     * id закупки
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "author_username", nullable = false)
    @NotNull
    @JsonIgnore         //нам пока что не надо выводить информацию по автору закупки
    private User author;


    /**
     * Наименование закупки
     */
    @NotNull
    private String name;

    /**
     * Описание закупки
     */
    @NotNull
    private String description;

    @NotNull
    @Column(name = "create_date")
    private final Date createDate;

    /**
     * Дата и время окончания срока подачи предложений
     * Маска для даты и времени: mask("ДД.ММ.ГГГГ:ЧЧ.ММ")
     */
    @NotNull
    private Date proposalDeadLine;

    /**
     * Дата окончания выполнения работ по закупке
     */
    @NotNull
    private Date finishDeadLine;

    /**
     * Бюджет закупки
     */
    @NotNull
    private BigInteger budget;

    /**
     * Общие требования по закупке
     */
    @NotNull
    private String demands;

    /**
     * Статус закупки (начата/завершена/отменена)
     */
    @NotNull
    @Column(name = "status")
    private PurchaseStatus status;

    /**
     * Причина отмены
     */
    private String cancelReason;

    /**
     * Состав команды
     */
    @NotNull
    private String team;

    /**
     * Условия работы
     */
    private String workCondition;

    //@JsonIgnore
    @OneToOne(mappedBy = "purchase")
    private FileEntity file;

    @OneToMany(mappedBy = "purchaseId")
    private List<SupplyEntity> supplies;

    public Purchase()
    {
        this.createDate = new Date();
    }

    public Purchase(User author, String name, String description, Date proposalDeadLine, Date finishDeadLine,
                    BigInteger budget, String demands, String team, String workCondition)
    {
        this.author = author;
        this.name = name;
        this.description = description;
        this.createDate = new Date();
        this.proposalDeadLine = proposalDeadLine;
        this.finishDeadLine = finishDeadLine;
        this.budget = budget;
        this.demands = demands;
        this.status = PurchaseStatus.bidAccepting;
        this.cancelReason = "";
        this.team = team;
        this.workCondition = workCondition;
    }
}
