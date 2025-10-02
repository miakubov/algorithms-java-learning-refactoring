package edu.t1;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public final class Res {
    
    /**
     * Сбрасывает значения полей объекта согласно конфигурации аннотации @Default.
     *
     * @param objects объекты, чьи поля будут сброшены
     */
    public static void reset(Object... objects) throws IllegalAccessException, InstantiationException {
        for (Object object : objects) {
            reset(object); // Обрабатываем объект отдельно
        }
    }

    private static void reset(Object object) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = object.getClass();
        
        // Проверяем наличие аннотации @Default
        if (!clazz.isAnnotationPresent(Default.class)) return;
        
        // Получаем класс конфигурации
        Class<?> configClass = clazz.getAnnotation(Default.class).value();
        
        Map<String, Object> defaultValues = extractDefaults(configClass); // извлекаем дефолтные значения
        
        setDefaultValues(object, defaultValues); // применяем дефолтные значения
    }

    /**
     * Возвращает карту значений полей из указанного класса конфигурации.
     *
     * @param configClass класс конфигурации
     * @return карта имен полей и их значений
     */
    private static Map<String, Object> extractDefaults(Class<?> configClass) throws IllegalAccessException, InstantiationException {
        Map<String, Object> defaults = new HashMap<>();
        Field[] declaredFields = configClass.getDeclaredFields();
        
        // Создаем экземпляр класса конфигурации
        Object instance = configClass.newInstance();
        
        for (Field field : declaredFields) {
            field.setAccessible(true); // открываем доступ к приватным полям
            defaults.put(field.getName(), field.get(instance)); // сохраняем значения полей
        }
        
        return defaults;
    }

    /**
     * Устанавливает значения полей объекта согласно карте дефолтных значений.
     *
     * @param object       объект, чье состояние обновляется
     * @param defaultValues карта имени поля и его дефолтного значения
     */
    private static void setDefaultValues(Object object, Map<String, Object> defaultValues) throws IllegalAccessException {
        Class<?> currentClass = object.getClass();
        
        while (currentClass != null) { // идём вверх по цепочке наследования
            Field[] declaredFields = currentClass.getDeclaredFields();
            
            for (Field field : declaredFields) {
                field.setAccessible(true); // разрешаем доступ к приватным полям
                
                String fieldName = field.getName();
                if (defaultValues.containsKey(fieldName)) {
                    field.set(object, defaultValues.get(fieldName));
                }
            }
            
            currentClass = currentClass.getSuperclass(); // переходим к суперклассу
        }
    }
}
