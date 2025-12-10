package com.example.spring_security.repository;

import com.example.spring_security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsRepository extends JpaRepository<User, Long> {


    @Query(value = """
            SELECT (
                json_build_object(
                    'registration', json_build_object(
                        'dataByMonth', (SELECT json_agg(registration_count ORDER BY month) FROM registration_month),
                        'stats', json_build_object(
                            'totalRegistration', (SELECT SUM(registration_count) FROM registration_month),
                            'avgMonthly', (SELECT ROUND(AVG(registration_count)) FROM registration_month),
                            'highestMonth', (SELECT CONCAT(registration_count, ' (', TO_CHAR(TO_DATE(month::text, 'MM'), 'Mon'), ')')
                                            FROM registration_month ORDER BY registration_count DESC LIMIT 1),
                            'growthPercentage',
                                (
                                WITH m AS (
                                    SELECT registration_count,
                                           LAG(registration_count) OVER (ORDER BY month) AS prev
                                    FROM registration_month
                                    ORDER BY month
                                )
                                SELECT CONCAT(
                                    ROUND(((registration_count - prev) * 100.0 / NULLIF(prev, 0))::numeric, 1), '%'
                                )
                                FROM m
                                WHERE prev IS NOT NULL
                                ORDER BY month DESC LIMIT 1
                                )
                        )
                    ),
                    'activeUsers', json_build_object(
                        'dataByMonth', (SELECT json_agg(active_user_count ORDER BY month) FROM active_users_month),
                        'stats', json_build_object(
                            'avgActivitiesMonthly', (SELECT ROUND(AVG(active_user_count)) FROM active_users_month),
                            'highestMonth', (SELECT CONCAT(active_user_count, ' (', TO_CHAR(TO_DATE(month::text, 'MM'), 'Mon'), ')')
                                             FROM active_users_month ORDER BY active_user_count DESC LIMIT 1),
                            'activityPercentage',
                                (
                                SELECT CONCAT(
                                    ROUND(
                                        (SUM(active_user_count)::numeric /
                                          (SELECT COUNT(*) FROM user_info) * 100
                                        ), 1
                                    ),
                                    '%'
                                )
                                FROM active_users_month
                                ),
                            'trend',
                                (
                                WITH t AS (
                                    SELECT month,
                                           active_user_count,
                                           LAG(active_user_count) OVER (ORDER BY month) AS prev
                                    FROM active_users_month
                                )
                                SELECT CASE
                                            WHEN active_user_count > prev THEN 'Rising'
                                            WHEN active_user_count < prev THEN 'Falling'
                                            ELSE 'Stable'
                                        END
                                FROM t
                                WHERE prev IS NOT NULL
                                ORDER BY month DESC LIMIT 1
                                )
                        )
                    )
                )
            )::text AS dashboard_stats;    
            """, nativeQuery = true)
    String getDashboardStats();

}
