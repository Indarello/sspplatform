package com.ssp.platform.repository;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.QuestionStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID>, JpaSpecificationExecutor<Question> {

    List<Question> findByPurchase(Purchase purchase, Sort sort);

    List<Question> findByPurchaseAndAuthorOrPurchaseAndPublicity(Purchase p1, User author, Purchase p2, QuestionStatus publicity, Sort sort);


}