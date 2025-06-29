/*SELECT*/
--Member
SELECT * FROM BackendUser
SELECT * FROM PwdReset
--Caregiver
SELECT * FROM caregivers
--RoomType	
SELECT * FROM RoomType
--Activity
SELECT * FROM Activity
--Rehabus
SELECT * FROM rehabus
--Device
SELECT * FROM DeviceCategory
SELECT * FROM Device


---------------------------------------------------------------------------------------------------------------------------------
/*CREATE SQL*/
--Member
CREATE TABLE BackendUser (
    user_ID          INT             IDENTITY(1,1)   PRIMARY KEY,       -- �ϥΪ̰ߤ@�ѧO
    user_name        NVARCHAR(50)    NOT NULL UNIQUE,                   -- ���u�m�W
    password        NVARCHAR(255)   NOT NULL,                           -- �K�X
    email           NVARCHAR(200)   NULL,                               -- Email
    role            NVARCHAR(50)    NOT NULL DEFAULT 'Admin',           -- ���������� (�v���]�w)
    is_active        BIT             NOT NULL DEFAULT 1,                -- �b���ҥΪ��A (1=�ҥ�, 0=����)
    createdAt       DATE       NOT NULL DEFAULT SYSUTCDATETIME(),		-- ���ɮɶ�
    updatedAt       DATE       NOT NULL DEFAULT SYSUTCDATETIME(),		-- ��Ƴ̫��s�ɶ�
);

CREATE TABLE PwdReset (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_ID INT NOT NULL,
    code CHAR(6) NOT NULL,
    expiresAt DATETIME2 NOT NULL,
    used BIT NOT NULL DEFAULT 0
);

--Caregiver
CREATE TABLE caregivers (
  caregiver_id      INT IDENTITY(1,1) PRIMARY KEY,        --�~�A��ID
  chineseName       NVARCHAR(100) NOT NULL,               --�~�A������W�r
  gender            BIT 	NOT NULL,                     --�ʧO
  phone             NVARCHAR(20) NOT NULL unique,         --�q��
  email             NVARCHAR(100) NOT NULL,			      --�q�l�H�c
  experience_years  INT NOT NULL,                         --�A�Ȧ~��
  photo			    NVARCHAR(MAX) 	                      --�ҥ��
  );
 
--RoomType
CREATE TABLE RoomType (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL,
    price INT NOT NULL,
    capacity INT NOT NULL, -- �쥻�� acapacity�A���O�]�� INT
    description NVARCHAR(500),
    special_features NVARCHAR(500),
    image_url NVARCHAR(255)
);
--Activity
CREATE TABLE Activity (
    id INT PRIMARY KEY IDENTITY(1,1),			       
    name NVARCHAR(100) NOT NULL,				       
    category NVARCHAR(50),                             
    limit INT,							               
    --participant INT DEFAULT 0,					       -- Delete
    date DATE NOT NULL,							       -- ���ʤ��
	time NVARCHAR(20),							       -- ���ʮɶ�
    location NVARCHAR(100),                            -- �W�Ҧa�I
    instructor NVARCHAR(100),                          -- �½ҦѮv
    status BIT DEFAULT 1,                              -- ���A�]�p�G1 = �i���W�B0 = �w�B���^
    --registrationstart DATE,                            -- Delete
    --registrationend DATE,                              -- Delete
    description NVARCHAR(MAX)                          -- ���ʻ���
);
--Rehabus
CREATE TABLE rehabus (

bus_id               INT   IDENTITY(1,1)     PRIMARY KEY               NOT NULL,           --�����y����
car_dealership      NVARCHAR(100)                                     NOT NULL,           --����
bus_brand           NVARCHAR(100)                                     NOT NULL,           --�T���t�P
bus_model           NVARCHAR(100)                                     NOT NULL,           --����
seat_capacity        INT                                               NOT NULL,           --�@��y��
wheelchair_capacity  INT                                               NOT NULL,           --���Ȯy��
license_plate        NVARCHAR(20)                                      NOT NULL  UNIQUE   --���P���X

);
--Device
CREATE TABLE DeviceCategory (
    id INT IDENTITY(1,1) PRIMARY KEY,  -- �������ID
    name NVARCHAR(255) NOT NULL,        -- �����W�� 
    category_id INT                       -- �����Ƨ�
);

CREATE TABLE Device (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,         --�ӫ~�W�� 
    sku NVARCHAR(50),                   -- �����Ƨǡ@�ӫ~�f��
    unitPrice DECIMAL(10,2),             ---���
    inventory INT,                        --�w�s 
    description NVARCHAR(MAX),            --�ӫ~�y�z
    image NVARCHAR(255),                   --�ӫ~�Ϥ�
    isOnline BIT,                         --�O�_�W�[
    category_id INT,                      -- �~��ѦҤ���
    CONSTRAINT fk_device_category FOREIGN KEY (category_id) REFERENCES DeviceCategory(id)
);

CREATE TABLE emp_log (
    id          BIGINT      IDENTITY(1,1) PRIMARY KEY,
    user_id     INT         NOT NULL,        -- �ާ@��
    action      VARCHAR(50) NOT NULL,        -- �ʧ@�N��
    target_id   INT         NULL,            -- �Q�ާ@����A�p���uID
    created_at  DATETIME2   NOT NULL DEFAULT SYSUTCDATETIME()
);
SELECT * FROM emp_log;
