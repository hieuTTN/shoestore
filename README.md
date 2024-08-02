## PROJECT WEBSITE BÁN GIÀY

### Công nghệ sử dụng
- Backend: Spring boot, spring security, oauth2, elasticsearch, cloudinary<br>
- Frontend: Html, css, javascript, ajax
- Database: mysql
- JDK: 17
- Hệ điều hành: Window, linux
- Công cụ: intellij, mysql workbench, elasticsearch tool 
### Các chức năng của project
3 tác nhân chính:<br>
<blockquote>
    - ADMIN: Là người có toàn bộ quyền đối với hệ thống<br>
    Các chức năng chính của admin:<br>
    + Quản lý tài khoản (Lọc, tìm kiếm, thay đổi quyền, khóa tài khoản)<br>
    + Quản lý danh mục (Thêm, sửa, xóa)<br>
    + Quản lý thương hiệu (Thêm, sửa, xóa)<br>
    + Quản lý banner (Thêm, sửa, xóa)<br>
    + Quản lý nhà cung cấp (Thêm, sửa, xóa)<br>
    + Quản lý sản phẩm (Thêm, sửa, xóa, xuất danh sách sản phẩm ra excel)<br>
    + Quản lý đơn hàng (Lọc đơn hàng, xem chi tiết, cập nhật trạng thái)
    + Quản lý nhập hàng (Thêm, sửa, xóa)<br>
    + Quản lý bài viết (Thêm, sửa, xóa)<br>
    + Quản lý doanh thu (xem doanh thu của 12 tháng)
</blockquote>

<blockquote>
    - Nhân viên: Là người có các quyền sau trong hệ thống<br>
    Các chức năng chính của nhân viên:<br>
    + Quản lý sản phẩm (xuất danh sách sản phẩm ra excel)<br>
    + Quản lý đơn hàng (Lọc đơn hàng, xem chi tiết, cập nhật trạng thái)
    + Quản lý nhập hàng (Thêm, sửa, xóa)<br>
    + Quản lý bài viết (Thêm, sửa, xóa)<br>
    + Quản lý nhà cung cấp (Thêm, sửa, xóa)<br>
</blockquote>

<blockquote>
    - Người dùng: Là người đã đăng nhập hoặc chưa đăng nhập<br>
    Các chức năng chính của người dùng:<br>
    + Xem danh sách sản phẩm, Xem chi tiết sản phẩm, lọc sản phẩm theo danh mục, thương hiệu, khoảng giá<br>
    + Xem bài viết, chi tiết bài viết<br>
    + Đặt hàng, thanh toán bằng momo hoặc khi nhận hàng<br>
    + Quản lý giỏ hàng (Thêm, sửa, xóa)<br>
    + Đăng nhập bằng gmail và mật khẩu hoặc đăng nhập bằng google<br>
    + Đăng ký tài khoản, gửi mã xác nhận qua email<br>
    + Quản lý sổ địa chỉ để không cần nhập thông tin khi đặt hàng (Thêm, sửa, xóa)<br>
    + Quản lý đơn hàng đã đặt<br>
    + Đổi mật khẩu<br>
</blockquote>

### Hướng dẫn cài đặt project
1. Cài đặt jdk 17 trước <br>
2. Cài đặt intellij và mở project web bằng cách chọn file -> open -> chọn đến thư mục project
3. Sau khi chọn project, vào lại file chọn project structure để chọn jdk cho project
4. Cài đặt mysql workbench và import file sql
5. vào file appication.properties để thay đổi url kết nối đến database
### Hướng dẫn cài đặt elasticsearch
- Điều kiện: Đã cài đặt máy ảo linux hoặc có 1 vps linux
- File hướng dẫn cài đặt: elasticsearch.txt