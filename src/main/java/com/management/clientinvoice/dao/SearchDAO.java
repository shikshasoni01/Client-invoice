package com.biz4solutions.clientinvoice.dao;

import com.biz4solutions.clientinvoice.dto.PaginationDTO;
import com.biz4solutions.clientinvoice.domain.Projects;
import com.biz4solutions.clientinvoice.dto.ProjectDTO;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;

@Repository
public class SearchDAO {

    private static SessionFactory sessionFactory;

    @Autowired
    public SearchDAO(EntityManagerFactory factory) {
        SearchDAO.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @Transactional(readOnly = true)
    public PaginationDTO<Projects> searchAllProjectsOrderByClient(Pageable pageable) {
        PaginationDTO<Projects> paginationDTO;


        final String FROM_CLAUSE_QUERY =new StringBuilder("FROM")
                .append("client_id cid")
                .append("INNER JOIN project_id pid ON (pid.client_id=cid.id)")
                .append("WHERE cid.is_active = true")
                .toString();

        paginationDTO = commonMethodForSearchDoctor(pageable, FROM_CLAUSE_QUERY);

        return paginationDTO;
    }


    private PaginationDTO<Projects> commonMethodForSearchDoctor(Pageable pageable, final String FROM_CLAUSE_QUERY) {

        PaginationDTO<Projects> paginationDTO = new PaginationDTO<>();

        final String[] COLS_AS = getSearchProjectsFieldsAs();
        final String SELECT_COLS = getSearchClientSelectClause(COLS_AS);
        final String COUNT = " COUNT(*) ";

        StringBuilder baseQueryBuilder = new StringBuilder("SELECT");
        baseQueryBuilder
                .append(SELECT_COLS)
                .append(FROM_CLAUSE_QUERY)

                // Order, Sort, Limit
                .append(" ORDER BY ").append(COLS_AS[2]).append(" ASC ")
                .append(" OFFSET ").append((pageable.getOffset()))
                .append(" LIMIT ").append(pageable.getPageSize())
                .append(";")
        ;

        int i = 0;
        paginationDTO.setList(sessionFactory.getCurrentSession()
                .createNativeQuery(baseQueryBuilder.toString())
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .addScalar(COLS_AS[i++], StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ProjectDTO.class)).list());

        StringBuilder countQueryBuilder = new StringBuilder("SELECT");
        countQueryBuilder
                .append(COUNT)
                .append(FROM_CLAUSE_QUERY)
                .append(";")
        ;
        NativeQuery countQuery = sessionFactory.getCurrentSession()
                .createNativeQuery(countQueryBuilder.toString());
        BigInteger bigIntCount = ((BigInteger) countQuery.uniqueResult());
        paginationDTO.setTotalCount(bigIntCount.longValue());
        paginationDTO.setTotalPages((long) Math.ceil(paginationDTO.getTotalCount() / (double) pageable.getPageSize()));
        return paginationDTO;
    }


    private String[] getSearchProjectsFieldsAs() {
        return new String[]{"id",
                "projectName",
                "description",
                "clients",
                "users",
                "startDate",
                "master_data",
                "is_active"};
    }


    private String getSearchClientSelectClause(String[] COLS_AS) {
        final String AS = " AS ";
        final String SEP = ", ";
        int i = 0;
        return new StringBuilder(" ")
                .append("CAST(pid.id").append(AS).append("VARCHAR)").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("CAST(cid.id").append(AS).append("VARCHAR)").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("cid.address1").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("pid.alternate_name").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("pid.FirstName").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("pid.client_id").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("pid.fax_no").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("pid.contact_no").append(AS).append(COLS_AS[i++]).append(SEP)
                .append("pid.email").append(AS).append(COLS_AS[i++]).append(SEP)
                .toString();
    }

}
