package com.example.testfetchdata.feature.procedure.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
@Slf4j
public class ProcedureRepositoryImpl implements ProcedureRepository{
    private final DataSource dataSource;

    public ProcedureRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void testGetDataStream() {
        try (Connection connection = this.dataSource.getConnection()){
            connection.setAutoCommit(false);
            String sql = "select * from data";
            /*
            * sql can be select * from func_get_data(?,?,?,?)
            * */
            CallableStatement stmt = connection.prepareCall(sql);
            stmt.setFetchSize(1000);
            ResultSet rs = stmt.executeQuery();
            while ((rs.next())){
                //TODO convert rs to data
            }
            connection.commit();
            /*
            * con:
            *   db create a temp table to store data
            *   client fetch data without use offset
            *   client fetch 1000 data and store in cache when index point to end of cursor continue fetch forward 1000
            * pro:
            *   client can hold connection too long
            *
            * => we can consume current data when wait to fetch another batch
            * */
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void executeProcedure() {
        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            String sql = "{call procedureName(?)}";
            //? is type refcursor
            CallableStatement stmt = connection.prepareCall(sql);
            stmt.setObject(1, null);
            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();
            Object refCursor = stmt.getObject(1);
            //TODO convert refCursor to data
            /*
            * get refCursor too long with extremely large data cause
            * in https://github.com/pgjdbc/pgjdbc/blob/master/pgjdbc/src/main/java/org/postgresql/jdbc/PgResultSet.java#L274
            * with type = refcursor stmt wait to fetch all data then close connection => we must wait to all data to consume
            *
            *
            * */
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
