/*Member*/
INSERT INTO BackendUser (user_name, password, email, role, is_active ) VALUES
('李大帥',    '123',    'admin@example.com',    'Admin',  1),
('王小貓',  '0',    'editor1@example.com',  'Editor', 1),
('李二',  '1',    'viewer1@example.com',  'Editor', 1),
('李三',  '2',    'testA@example.com',    'Editor', 1),
('李四',  '3',    'testB@example.com',    'Editor', 1),
('王宗二',    '123',    'admin@example.com',    'Editor',  1),
('王一一',  '0',    'editor1@example.com',  'Editor', 1),
('劉三',  '1',    'viewer1@example.com',  'Editor', 1),
('李六',  '2',    'testA@example.com',    'Editor', 1),
('李戚戚',  '3',    'testB@example.com',    'Editor', 1);

/*Caregiver*/
INSERT INTO caregivers (chineseName, gender, phone, email, experience_years, photo) VALUES
(N'李志強', 0, N'0922333444', N'li.zhiqiang@example.com', 8, N'/CaregiverPage/upload/photo/s51b45b4f-2ded-40c7-af70-4727e65df86a.jpg'),
(N'張天佳', 1, N'0955667788', N'zhang.tianjia@example.com', 10, N'/CaregiverPage/upload/photo/s6d42a20a-cace-4b4f-b876-236c904c306.jpg'),
(N'許立德', 0, N'0966896901', N'xu.lide@example.com', 2, N'/CaregiverPage/upload/photo/s7b0ac8dd-e470-4c4e-b6f7-41480f3960e662.jpg'),
(N'陈美華', 1, N'0912344678', N'chen.meihua@example.com', 5, N'/CaregiverPage/upload/photo/s87094fdc-4c57-4c4a-b6f5-fc78d8eac48d.jpg'),
(N'陳美妹', 0, N'0999888755', N'chen.meimei@gmail.com', 5, N'/CaregiverPage/upload/photo/sd9a80589-f86a-4f32-ab4a-535ff5a79811.jpg'),
(N'沈時', 1, N'0912456789', N'shen.shi@gmail.com', 14, N'/CaregiverPage/upload/photo/s33e8bff5-e8ea-4659-b8b5-26ed0be59e2f.jpg'),
(N'許宗青', 1, N'0967877777', N'xu.zongqing@gmail.com', 10, N'/CaregiverPage/upload/photo/se67f-4806-b657-d8b13753ed530e.jpg'),
(N'許富', 1, N'0925252252', N'xu.fu@gmail.com', 6, N'/CaregiverPage/upload/photo/s7d8911ed-ac3c-49c5-aa25-734a05d1a749.jpg'),
(N'許伊伊', 0, N'0925564475', N'xu.yiyi@example.com', 8, N'/CaregiverPage/upload/photo/s62ed9ba4-5629-490c-b57a-60c5da4cc16a.jpg');

/*RoomType*/
INSERT INTO RoomType (name, price, capacity, description, special_features, image_url) VALUES
('單人房', 22000, 1, '提供基本照護設施，適合偏好安靜環境的長者', '冷氣、衣櫃、冰箱、電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room1.png'),
('雙人房', 20000, 2, '二人共住，具備簡單隱私設計，適合陪伴與交流', '冷氣、個人衣櫃、冰箱、電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room2.png'),
('四人房', 17000, 4, '空間寬敞，適合社交與團體生活', '冷氣、共用衣櫃、冰箱、共用電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room3.png'),
('六人房', 15000, 6, '多人共住，注重照護便利與互動，經濟實惠', '冷氣、共用空間、共用衣櫃、冰箱、共用電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room4.png'),
('無障礙單人房', 25000, 1, '為行動不便者設計，無門檻、寬門、無障礙衛浴，提升生活便利與安全', '無障礙衛浴、無門檻設計、緊急呼叫鈴、冷氣、冰箱、電視、扶手', 'RoomTypePage/RoomImg/room5.png'),
('夫妻房／雙人套房', 28000, 2, '適合伴侶共住，具備分離式床鋪與獨立衛浴，兼顧私密與舒適', '獨立衛浴、雙衣櫃、冷氣、冰箱、電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room6.png'),
('失智症專區房型', 20000, 1, '設有安全防護與環境導引標示，降低迷失風險，適合輕中度失智長者', '冷氣、電視、簡化家具、導引標示、安全門、緊急呼叫鈴', 'RoomTypePage/RoomImg/room6.png'),
('療養房（醫療型）', 30000, 1, '配備醫療設備與照護支援，適合需長期照護或術後療養之住民', '醫療床、冷氣、獨立衛浴、緊急呼叫鈴、醫療插座、冰箱、電視', 'RoomTypePage/RoomImg/room6.png'),
('豪華單人房', 32000, 1, '提供獨立景觀陽台與升級家具，適合追求生活品質的長者', '冷氣、加大床、衣櫃、冰箱、電視、緊急呼叫鈴、景觀陽台、高級書桌', 'RoomTypePage/RoomImg/room6.png'),
('尊爵單人房', 38000, 1, '提供飯店式服務與房內照護人員呼叫系統，享有高隱私與便利性', '冷氣、冰箱、電視、獨立衛浴、緊急呼叫鈴、24小時照護對講、沙發椅', 'RoomTypePage/RoomImg/room6.png'),
('豪華雙人房', 36000, 2, '寬敞舒適的雙人空間，附有獨立衛浴與雙開窗設計', '冷氣、雙人床、雙衣櫃、冰箱、電視、獨立衛浴、緊急呼叫鈴、閱讀燈', 'RoomTypePage/RoomImg/room6.png'),
('尊爵雙人房', 42000, 2, '飯店式套房設計，附帶客廳區與簡易廚房，提升居住自由度', '冷氣、雙人床、獨立客廳、簡易廚房、冰箱、電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room6.png'),
('高級四人房', 26000, 4, '每床設置隔簾與個人櫃，兼顧隱私與社交', '冷氣、隔簾床位、個人櫃、共用冰箱、共用電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room6.png'),
('尊爵四人房', 30000, 4, '升級照護空間，每位住民享有半隔間與獨立閱讀燈', '冷氣、半隔間設計、閱讀燈、個人衣櫃、冰箱、共用電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room6.png'),
('高級六人房', 22000, 6, '空間規劃良好，提供多重收納設計與窗景座位區', '冷氣、大型共用衣櫃、窗邊座位、冰箱、電視、緊急呼叫鈴', 'RoomTypePage/RoomImg/room6.png'),
('尊爵六人房', 26000, 6, '附帶活動空間與照護支援服務，提供經濟與舒適兼顧的選擇', '冷氣、共用休息區、照護對講、共用衣櫃、冰箱、共用電視、緊急呼叫鈴','RoomTypePage/RoomImg/room6.png');

/*Activity*/
INSERT INTO Activity (name, category, limit, date, time, location, instructor, status, description) VALUES
('瑜珈初級班', '運動健身', 20, '2025-07-15', '09:00-10:30', '健身房A室', '林美玲', 1, '適合初學者的基礎瑜珈課程，教授基本體位法和呼吸技巧'),
('手機攝影技巧', '藝術創作', 15, '2025-07-20', '14:00-16:00', '多媒體教室', '張志強', 1, '學習如何用手機拍出專業級照片，包含構圖、光線運用等技巧'),
('烘焙入門：戚風蛋糕', '烹飪料理', 12, '2025-07-25', '10:00-12:00', '烘焙教室', '王小華', 0, '從零開始學習戚風蛋糕製作，包含材料選擇、攪拌技巧和烘烤要點'),
('太極拳晨練', '運動健身', 25, '2025-07-18', '06:30-07:30', '公園廣場', '陳師父', 1, '傳統太極拳24式教學，適合各年齡層參與，有助身心健康'),
('水彩畫風景寫生', '藝術創作', 18, '2025-07-22', '08:00-11:00', '植物園', '李藝術', 1, '戶外水彩寫生課程，學習風景畫技法和色彩運用'),
('咖啡沖煮工作坊', '烹飪料理', 10, '2025-07-28', '15:00-17:00', '咖啡實驗室', '黃咖啡', 1, '學習手沖咖啡技巧，認識不同產區豆子特色和萃取方法'),
('兒童足球訓練', '運動健身', 30, '2025-07-19', '16:00-17:30', '足球場', '劉教練', 1, '6-12歲兒童足球基礎訓練，培養團隊合作精神和運動技能'),
('陶藝手拉坯體驗', '藝術創作', 8, '2025-07-26', '13:00-15:30', '陶藝工作室', '蕭陶師', 0, '親手體驗陶藝創作樂趣，學習手拉坯基本技法'),
('家常菜料理班', '烹飪料理', 16, '2025-07-24', '18:30-20:30', '料理教室', '媽媽味', 1, '教授經典家常菜料理，包含紅燒肉、糖醋排骨等經典菜色'),
('羽毛球進階班', '運動健身', 14, '2025-07-30', '19:00-21:00', '體育館', '林羽球', 1, '適合有基礎的學員，著重戰術運用和高階技巧訓練');

/*Rehabus*/
INSERT INTO rehabus (car_dealership, bus_brand, bus_model, seat_capacity, wheelchair_capacity, license_plate)
VALUES
  ('Taipei Motors',  'Toyota',    'Coaster', 20, 2, 'AB-1234'),
  ('Kaohsiung Auto', 'Ford',      'Transit', 18, 3, 'AC-2345'),
  ('Taichung Wheels','Mercedes',  'Sprinter',15, 4, 'AD-3456'),
  ('Tainan Vehicle', 'Nissan',    'Civilian',12, 1, 'AE-4567'),
  ('Hualien Mobility','Volkswagen','Crafter',16, 2, 'AF-5678');

/*Device*/
INSERT INTO DeviceCategory (name, category_id) VALUES 
(N'輪椅', 1),
(N'拐杖', 2),
(N'助行器', 3),
(N'電動床', 4),
(N'洗澡椅', 5),
(N'護理床', 6),
(N'病人移位機', 7),
(N'床邊扶手', 8),
(N'便盆椅', 9),
(N'防滑墊', 10);

INSERT INTO Device (name, sku, unitPrice, inventory, description, image, isOnline, category_id) VALUES 
(N'手動輪椅', N'SKU001', 3500.00, 25, N'輕便可折疊手動輪椅，適合室內外使用', N'/images/wheelchair1.jpg', 1, 1),
(N'鋁合金拐杖', N'SKU002', 450.00, 100, N'人體工學握把設計，適用於行走輔助', N'/images/cane1.jpg', 1, 2),
(N'三腳助行器', N'SKU003', 1200.00, 40, N'穩固設計，提供額外支撐力', N'/images/walker1.jpg', 1, 3),
(N'電動病床', N'SKU004', 18500.00, 10, N'可調整床面角度，附護欄', N'/images/bed1.jpg', 0, 4),
(N'洗澡椅（可折疊）', N'SKU005', 980.00, 60, N'適合長者沐浴時使用，防滑設計', N'/images/bathchair1.jpg', 1, 5),
(N'多功能護理床', N'SKU006', 21500.00, 5, N'附遙控與防夾功能，適合長照機構', N'/images/nursingbed1.jpg', 0, 4),
(N'移位機（電動）', N'SKU007', 32000.00, 3, N'協助照顧者安全移動患者', N'/images/hoist1.jpg', 1, 6),
(N'床邊扶手', N'SKU008', 780.00, 80, N'安裝簡易，防止跌倒', N'/images/rail1.jpg', 1, 8),
(N'便盆椅', N'SKU009', 1100.00, 50, N'可拆洗便盆設計，方便清潔', N'/images/commode1.jpg', 1, 9),
(N'防滑地墊', N'SKU010', 360.00, 200, N'浴室與走道適用，防滑材質', N'/images/mat1.jpg', 1, 10);