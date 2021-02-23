package dev.sim0n.anticheat.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

@UtilityClass
public class ReflectionUtil {

    /**
     * Gets a field in {@param clazz}
     * @param clazz The class owning the class
     * @param fieldName The name of the field to get
     * @return The field
     */
    public Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(clazz.getSimpleName() + ":" + fieldName);
        }
    }

    /**
     * Gets the field value of {@param field}
     * @param field The field to get
     * @param instance The object instance (null if static)
     * @return The field value
     */
    public <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets the value of {@param field} to {@param value}
     * @param field The field to set the value of
     * @param instance The object instance (null if static)
     * @param value The value to set {@param field} to
     */
    public void setFieldValue(Field field, Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    public <T extends AccessibleObject> T setAccessible(T object) {
        object.setAccessible(true);
        return object;
    }
}

