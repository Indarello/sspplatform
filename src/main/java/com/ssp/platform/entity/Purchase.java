package com.ssp.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssp.platform.entity.enums.PurchaseStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

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

    /**
     * Дата и время создания закупки
     * Все даты храним в Long, так удобнее для фронтендера
     * это timestamp в секундах
     */
    @NotNull
    private Long createDate;

    /**
     * Дата и время окончания срока подачи предложений
     */
    @NotNull
    private Long proposalDeadLine;

    /**
     * Дата окончания выполнения работ по закупке
     */
    @NotNull
    private Long finishDeadLine;

    /**
     * Бюджет закупки
     */
    @NotNull
    private Long budget;

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
    @NotNull
    private String cancelReason;

    /**
     * Состав команды
     */
    @NotNull
    private String team;

    /**
     * Условия работы
     */
    @NotNull
    private String workCondition;

    @OneToMany(mappedBy = "purchase", targetEntity = FileEntity.class)
    private List<FileEntity> files;

    /**
     * Массив сущностей предложений
     */
    @OneToMany(mappedBy = "purchase")
    @JsonIgnore
    private List<SupplyEntity> supplies;

    public Purchase()
    {
        this.createDate = System.currentTimeMillis()/1000;
    }

    public Purchase(User author, String name, String description, Long proposalDeadLine, Long finishDeadLine,
                    Long budget, String demands, String team, String workCondition)
    {
        this.author = author;
        this.name = name;
        this.description = description;
        this.createDate = System.currentTimeMillis()/1000;
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
