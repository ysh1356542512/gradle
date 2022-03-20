import org.gradle.launcher.GradleMain
import org.gradle.launcher.daemon.bootstrap.GradleDaemon
import org.gradle.wrapper.GradleWrapperMain

fun main(args: Array<String>) {
//     GradleWrapperMain.main(args)
//     GradleMain.main(args)
     GradleDaemon.main(args)
}