package com.example.testfetchdata.feature.procedure.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            * sql con be select * from func_get_data(?,?,?,?)
            * */
            CallableStatement stmt = connection.prepareCall(sql);
            stmt.setFetchSize(1000);
            ResultSet rs = stmt.executeQuery();
            while ((rs.next())){
                //TODO convert rs to data
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
