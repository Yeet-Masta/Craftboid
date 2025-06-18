import java.lang.reflect.Method

/**
 * **ReflectionUtils** TODO: Document.
 *
 * @author Jab
 */
object ReflectionUtils {

  fun getAllDeclaredMethods(clazz: Class<*>): List<Method> {
    val methods = ArrayList<Method>()
    var c: Class<*>? = clazz
    while (c != null) {
      methods.addAll(listOf(*c.declaredMethods))
      c = c.superclass
    }
    return methods
  }
}