package net.sales_history;

public interface AttributeConverter {
    public Integer convertToDatabaseColumn(String attribute);
    public String convertToEntityAttribute(Integer dbData);
}