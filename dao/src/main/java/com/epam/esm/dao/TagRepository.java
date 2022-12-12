package com.epam.esm.dao;

import com.epam.esm.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends BaseRepository<Tag> {
    String NAME_FIELD_NAME = "name";

    Optional<Tag> findByName(String name);

    @Query(
            value = """
                        WITH tags_by_top AS (
                            SELECT t.id id, t.name name, count(t.id) id_count
                            FROM (SELECT user_id
                                  FROM (SELECT sum(total) total_sum,
                                               user_id
                                        FROM user
                                                 JOIN `order` o
                                                      ON user.id = o.user_id
                                        GROUP BY user_id) totals
                                  ORDER BY total_sum DESC
                                  LIMIT 1) u
                                     LEFT JOIN `order` o
                                               ON u.user_id = o.user_id
                                     LEFT JOIN gift_certificate_orders ohgc
                                               ON o.id = ohgc.order_id
                                     LEFT JOIN gift_certificate_tags gcht
                                               ON ohgc.gift_certificate_id = gcht.gift_certificate_id
                                     LEFT JOIN tag t
                                               ON gcht.tag_id = t.id
                            WHERE u.user_id = o.user_id
                            GROUP  BY t.id, t.name
                        )
                        
                        SELECT id, name
                        FROM tags_by_top
                        WHERE id_count = (SELECT max(id_count) FROM tags_by_top)
                    """,
            nativeQuery = true)
    Page<Tag> findMostUsedTagsOfUserWithHighestCostOfAllOrders(Pageable pageable);

}
