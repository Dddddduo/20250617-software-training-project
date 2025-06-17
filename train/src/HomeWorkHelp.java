import java.sql.*;
import java.util.*;

public class HomeWorkHelp {
    private static final String URL = "jdbc:mysql://182.92.125.34:3306/train";
    private static final String USER = "dduo";
    private static final String PASSWORD = "2JmJBD5MynXxDCrA";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) {
            // 注册驱动并获取连接
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("连接成功!");
                boolean bol = true;
                while (bol) {
                    printMenu();
                    int choice = getValidChoice(scan, 1, 7);
                    switch (choice) {
                        case 1 -> addProduct(conn, scan);
                        case 2 -> viewAllProducts(conn);
                        case 3 -> viewProductByName(conn, scan);
                        case 4 -> updateProduct(conn, scan);
                        case 5 -> deleteProduct(conn, scan);
                        case 6 -> calculateTotalPrice(conn);
                        case 7 -> {
                            bol = false;
                            System.out.println("谢谢使用!");
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("数据库操作异常: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("驱动加载失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 打印菜单
    private static void printMenu() {
        System.out.println("商品管理系统\n" +
                "1. 录入商品入库\n" +
                "2. 查看所有商品信息\n" +
                "3. 查看指定名称的商品信息\n" +
                "4. 根据编号修改商品信息\n" +
                "5. 根据编号删除商品信息\n" +
                "6. 统计仓库中所有商品的总计价格\n" +
                "7. 退出系统");
        System.out.print("请您输入功能序号: ");
    }

    // 获取有效选择
    private static int getValidChoice(Scanner scan, int min, int max) {
        int choice;
        while (true) {
            try {
                choice = scan.nextInt();
                scan.nextLine(); // 消耗换行符
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.printf("请输入%d-%d之间的数字: ", min, max);
                }
            } catch (java.util.InputMismatchException e) {
                System.out.printf("请输入%d-%d之间的数字: ", min, max);
                scan.nextLine(); // 清除无效输入
            }
        }
    }

    // 添加商品
    private static void addProduct(Connection conn, Scanner scan) throws SQLException {
        System.out.print("请您输入商品的名称: ");
        String sname = scan.nextLine();
        System.out.print("请您输入商品的价格: ");
        double sprice = scan.nextDouble();
        scan.nextLine(); // 消耗换行符
        System.out.print("请您输入商品的类别(默认为其他): ");
        String scategory = scan.nextLine();
        if (scategory.isEmpty()) scategory = "其他";
        System.out.print("请您输入商品的库存数量: ");
        int sinventory = scan.nextInt();

        String sql = "INSERT INTO sp (sname, sprice, scategory, sinventory) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sname);
            pstmt.setDouble(2, sprice);
            pstmt.setString(3, scategory);
            pstmt.setInt(4, sinventory);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "操作成功!" : "操作失败!");
        }
    }

    // 查看所有商品
    private static void viewAllProducts(Connection conn) throws SQLException {
        String sql = "SELECT sid, sname, sprice, scategory, sinventory FROM sp ORDER BY sid";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n编号\t名称\t\t价格\t\t类别\t\t库存");
            while (rs.next()) {
                System.out.printf("%d\t%s\t\t%.2f\t\t%s\t\t%d\n",
                        rs.getInt("sid"),
                        rs.getString("sname"),
                        rs.getDouble("sprice"),
                        rs.getString("scategory"),
                        rs.getInt("sinventory"));
            }
        }
    }

    // 查看指定名称的商品
    private static void viewProductByName(Connection conn, Scanner scan) throws SQLException {
        System.out.print("请输入要查询的商品名称: ");
        String sname = scan.nextLine();

        String sql = "SELECT sid, sname, sprice, scategory, sinventory FROM sp WHERE sname LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + sname + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("未找到相关商品!");
                    return;
                }
                System.out.println("\n编号\t名称\t\t价格\t\t类别\t\t库存");
                while (rs.next()) {
                    System.out.printf("%d\t%s\t\t%.2f\t\t%s\t\t%d\n",
                            rs.getInt("sid"),
                            rs.getString("sname"),
                            rs.getDouble("sprice"),
                            rs.getString("scategory"),
                            rs.getInt("sinventory"));
                }
            }
        }
    }

    // 更新商品信息
    private static void updateProduct(Connection conn, Scanner scan) throws SQLException {
        System.out.print("请输入要修改的商品编号: ");
        int sid = scan.nextInt();
        scan.nextLine(); // 消耗换行符

        // 检查商品是否存在
        if (!checkProductExists(conn, sid)) {
            System.out.println("商品不存在!");
            return;
        }

        System.out.print("请输入新的商品名称(不修改请直接回车): ");
        String sname = scan.nextLine();
        System.out.print("请输入新的商品价格(不修改请直接回车): ");
        String spriceInput = scan.nextLine();
        System.out.print("请输入新的商品类别(不修改请直接回车): ");
        String scategory = scan.nextLine();
        System.out.print("请输入新的库存数量(不修改请直接回车): ");
        String sinventoryInput = scan.nextLine();

        StringBuilder sql = new StringBuilder("UPDATE sp SET ");
        List<Object> params = new ArrayList<>();
        boolean hasUpdate = false;

        if (!sname.isEmpty()) {
            sql.append("sname = ?, ");
            params.add(sname);
            hasUpdate = true;
        }
        if (!spriceInput.isEmpty()) {
            sql.append("sprice = ?, ");
            params.add(Double.parseDouble(spriceInput));
            hasUpdate = true;
        }
        if (!scategory.isEmpty()) {
            sql.append("scategory = ?, ");
            params.add(scategory);
            hasUpdate = true;
        }
        if (!sinventoryInput.isEmpty()) {
            sql.append("sinventory = ?, ");
            params.add(Integer.parseInt(sinventoryInput));
            hasUpdate = true;
        }

        if (!hasUpdate) {
            System.out.println("未修改任何信息!");
            return;
        }

        // 移除最后一个逗号
        sql.delete(sql.length() - 2, sql.length());
        sql.append(" WHERE sid = ?");
        params.add(sid);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "修改成功!" : "修改失败!");
        }
    }

    // 删除商品
    private static void deleteProduct(Connection conn, Scanner scan) throws SQLException {
        System.out.print("请输入要删除的商品编号: ");
        int sid = scan.nextInt();
        scan.nextLine(); // 消耗换行符

        if (!checkProductExists(conn, sid)) {
            System.out.println("商品不存在!");
            return;
        }

        System.out.print("确定要删除该商品吗？(Y/N): ");
        String confirm = scan.nextLine();
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("已取消删除操作!");
            return;
        }

        String sql = "DELETE FROM sp WHERE sid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sid);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "删除成功!" : "删除失败!");
        }
    }

    // 统计总价格
    private static void calculateTotalPrice(Connection conn) throws SQLException {
        String sql = "SELECT SUM(sprice * sinventory) AS total_price FROM sp";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                double totalPrice = rs.getDouble("total_price");
                System.out.printf("仓库中所有商品的总计价格为: %.2f 元\n", totalPrice);
            } else {
                System.out.println("暂无商品数据!");
            }
        }
    }

    // 检查商品是否存在
    private static boolean checkProductExists(Connection conn, int sid) throws SQLException {
        String sql = "SELECT 1 FROM sp WHERE sid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sid);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}