package dynamic_type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 使用java.long.invoke实现动态类型
 * 有点类似于函数指针。
 */
public class MethodHandleTest {
    static class ClassA {
        public void println(String s) {
            System.out.println("ClassA" + s);
        }
    }

    public static void main(String[] args) throws Throwable {
        boolean flag = ((int) ((Math.random() * 10)) % 2 == 0);
        Object obj = flag ? System.out : new ClassA();
        getPrintlnMH(obj).invokeExact(flag + " test");
    }

    private static MethodHandle getPrintlnMH(Object reciver) throws Throwable {
        /* methodtype : 方法类型，第一个参数代表返回值， 后面的参数代表具体的方法参数*/
        MethodType methodType = MethodType.methodType(void.class, String.class);

        // MethodHandles.lookup() 在制定的类中查找对应的方法，要求符合：方法名称、方法参数、并符合调用权限
        // 按照java语言规则，方法的第一个参数是隐式的，代表this指向的对象，这个参数以前是在参数列表中进行传递的，现在提供bindTo(reciver)来完成
        return MethodHandles.lookup().findVirtual(reciver.getClass(), "println", methodType).bindTo(reciver);
    }
}
