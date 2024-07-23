package TestProjectDAO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MaintenanceDAO {

    public JdbcTemplate jdbcTemplate;

    public MaintenanceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Example how to get String using JdbcTemplate
    public String getCustomerSql() {
        String sql = " select * from customer";
        jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                System.out.println("============== " + rs.getString(2
                ) + " ==================");
                return rs.getString(2);
            }
        });
        return null;
    }

    // Example how to get all the rows of the table into List of Maps. This way simplifies database result mapping.
    public List<Map<String, Object>> getAllCustomers() {
        String sql = " select * from customer";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> result : data) {
            System.out.println(" | " + result.get("cust_no") + " | " + result.get("cust_name") + " | " + result.get("cust_hdqtr_office_no") + " | " + result.get("cust_status_name") + " | ");
        }
        return data;

    }

    public int getTableRecordCount(String tableName) {
        String sql = " select * from " + tableName;
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        return data.size();
    }

    public List<Map<String, Object>> getAllRecordsFromTable(String username, String ref) {
        String sql = " select *from sk_repair_queue_audit where last_upd_user_id ='" + username + "' and ref_no='" + ref + "' order by audit_id desc";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        return data;
    }

    public int getRepairQueBadgeRecordCount() {
        String sql = "select  count(*) as OpenRecords from sk_repair_queue_view where status_desc=" + "'Open'" + " ";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> result : data) {
            System.out.println(" ======================== " + result.get("OpenRecords"));
            return Integer.parseInt(result.get("OpenRecords").toString());
        }
        return 0;
    }

    public int getApprovalBadgeRecordCount() {
        String sql = "select  count(*) as PendingRecords from sk_appvl_queue_view";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> result : data) {
            System.out.println(" ======================== " + result.get("PendingRecords"));
            return Integer.parseInt(result.get("PendingRecords").toString());
        }
        return 0;
    }
    public List<Map<String, Object>> getActivePledgeOrAssetCode(String tableName, String flag){
        String sql = "select * from "+tableName+" where activ_code='"+flag+"' ";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        return data;
    }


}