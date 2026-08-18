# BÁO CÁO BÀI TẬP: TỐI ƯU PROMPT NGĂN LỖI ĐỊNH DẠNG BEANOUTPUTCUTCONVERTER

## PHẦN 1: TIÊU ĐỀ BÀI TẬP VÀ TÓM TẮT YÊU CẦU
- **Tên bài tập:** Bài 1: Tối ưu Prompt — Ngăn lỗi định dạng BeanOutputConverter
- **Mã số sinh viên:** nguyen0605_PTIT-HCM-059
- **Tóm tắt yêu cầu:**
  - Khắc phục lỗi LLM (đặc biệt là các local model như Qwen 7B) tự ý chèn thẻ markdown fence (```json ... ```) hoặc văn bản tự do khi sử dụng `BeanOutputConverter`.
  - Thiết kế một Prompt nâng cao đầy đủ cấu trúc (Vai trò, Mục tiêu, Ngữ cảnh, Ràng buộc nghiêm ngặt, Định dạng đầu ra) chứa 2 tham số: `{email}` và `{formatInstructions}`.
  - Xây dựng giải pháp mã nguồn Java hoàn chỉnh tích hợp và kiểm thử.
  - Mô phỏng thực tế cuộc trò chuyện với AI để chứng minh tính ổn định của Prompt.

## PHẦN 2: GIẢ LẬP CUỘC TRÒ CHUYỆN THỰC TẾ VỚI AI
### 1. Câu lệnh Prompt nâng cao gửi đi
```
[ROLE]
You are an elite, zero-error data extraction engine designed specifically to parse structured data for machine APIs. Your primary directive is to output ONLY raw JSON compliance data without any fluff.

[OBJECTIVE]
Extract the following fields from the client email provided in [INPUT EMAIL]:
- Customer Name (tên khách hàng)
- Phone Number (số điện thoại liên hệ)

[INPUT EMAIL]
Chào shop, mình tên là Nguyễn Văn A, số điện thoại của mình là 0987654321. Nhờ shop check giúp đơn hàng của mình nhé!

[STRICT SYSTEM CONSTRAINTS - MANDATORY]
1. Output MUST be a single raw JSON block only. DO NOT wrap the output in markdown code blocks (e.g. no ```json, no ```).
2. NO preambles, NO introductory text, NO polite greetings, and NO conversational postscripts. Absolute silence other than the raw JSON payload.
3. The output must strictly begin with '{' and end with '}'.
4. If a field is missing, set its value to null.

[FORMAT INSTRUCTIONS]
Your output must be a JSON object containing the properties:
- customerName: String representing the name of the customer
- phoneNumber: String representing the phone number
```

### 2. Phản hồi sạch từ AI (Không có Markdown backticks hay text rác)
```json
{"customerName": "Nguyễn Văn A", "phoneNumber": "0987654321"}
```

## PHẦN 3: GIẢ THÍCH PHƯƠNG PHÁP TỐI ƯU
1. **Phân vùng thông tin rõ ràng:** Sử dụng các thẻ cấu trúc như `[ROLE]`, `[OBJECTIVE]`, `[STRICT SYSTEM CONSTRAINTS]` để mô hình dễ nhận diện và ưu tiên luồng xử lý.
2. **Kỹ thuật Triệt tiêu lỗi Markdown:** Chỉ rõ việc cấm dùng ký tự đặc biệt và ra lệnh im lặng hoàn toàn ("Absolute silence").
3. **Phòng thủ đa tầng (Multi-layer Defense):** Trong mã nguồn Java, thiết lập thêm phương thức `cleanJsonIfNeeded` để loại bỏ thủ công các ký tự markdown nếu mô hình thỉnh thoảng vẫn vi phạm, giúp tăng độ an toàn cho hệ thống.