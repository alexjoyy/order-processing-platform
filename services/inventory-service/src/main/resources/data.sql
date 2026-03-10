INSERT INTO inventory_items (product_code, available_quantity)
VALUES ('JAVA-BOOK', 100), ('SPRING-COURSE', 60), ('MICROSERVICE-KIT', 40)
ON CONFLICT (product_code) DO NOTHING;
