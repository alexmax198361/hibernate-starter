package dao;

import dto.CompanyDto;
import entity.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.Tuple;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoCriteria {

    private static final UserDaoCriteria INSTANCE = new UserDaoCriteria();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user);
        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user).where(
                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstName), firstName));
        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {

        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user).orderBy(cb.asc(user.get(User_.personalInfo)));

        return session.createQuery(criteria).setMaxResults(limit).list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);
        var company = criteria.from(Company.class);
        var users = company.join(Company_.users);

        criteria.select(users).where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        var cb = session.getCriteriaBuilder();

        var criteria = cb.createQuery(Payment.class);
        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);
        var company = user.join(User_.company);
        criteria.select(payment)
                .where(cb.equal(company.get(Company_.name), companyName))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstName)),
                        cb.asc(payment.get(Payment_.amount)));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Double.class);

        var payment = criteria.from(Payment.class);
        var user = payment.join(Payment_.receiver);

        criteria.select(cb.avg(payment.get(Payment_.amount)))
                .where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstName), firstName),
                        cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastName), lastName))
        ;
        return session.createQuery(criteria).getSingleResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CompanyDto.class);
        var company = criteria.from(Company.class);
        var user = company.join(Company_.users, JoinType.INNER);
        var payment = user.join(User_.payments);

        criteria.select(cb.construct(CompanyDto.class, company.get(Company_.name), cb.avg(payment.get(Payment_.amount))))
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Tuple.class);

        var subquery = criteria.subquery(Double.class);
        var paymentSubquery = subquery.from(Payment.class);

        Root<User> user = criteria.from(User.class);
        var payment = user.join(User_.payments);
        criteria
                .select(cb.tuple(user, cb.avg(payment.get(Payment_.amount))))
                .groupBy(user.get(User_.id))
                .having(cb.gt(cb.avg(payment.get(Payment_.amount)), subquery.select(cb.avg(paymentSubquery.get(Payment_.amount))))
                );
        return session.createQuery(criteria).list();
    }

    public static UserDaoCriteria getInstance() {
        return INSTANCE;
    }
}
