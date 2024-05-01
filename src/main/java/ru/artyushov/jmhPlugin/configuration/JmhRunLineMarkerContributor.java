package ru.artyushov.jmhPlugin.configuration;

import com.intellij.execution.lineMarker.ExecutorAction;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UastUtils;

import static com.intellij.icons.AllIcons.Actions.ProfileYellow;
import static ru.artyushov.jmhPlugin.configuration.ConfigurationUtils.isBenchmarkClass;
import static ru.artyushov.jmhPlugin.configuration.ConfigurationUtils.isBenchmarkMethod;

/**
 * @author Sergey Sitnikov
 */
public class JmhRunLineMarkerContributor extends RunLineMarkerContributor {

    @Nullable
    @Override
    public Info getInfo(@NotNull PsiElement psiElement) {
        UElement uElement = UastUtils.getUParentForIdentifier(psiElement);
        if (uElement instanceof UMethod) {
            boolean isBenchmarkMethod = isBenchmarkMethod((UMethod) uElement);
            if (isBenchmarkMethod) {
                // FIXME use something similar to com.intellij.sh.run.ShRunFileAction
                // FIXME for some reason this doesn't work anymore https://github.com/artyushov/idea-jmh-plugin/issues/50
//                final AnAction[] actions = new AnAction[]{ActionManager.getInstance().getAction("RunClass")};
                AnAction[] actions = ExecutorAction.getActions(1);
                return new Info(ProfileYellow, actions, null);
            }
        } else if (uElement instanceof UClass) {
            boolean isBenchmarkClass = isBenchmarkClass((UClass) uElement);
            if (isBenchmarkClass) {
                // FIXME use something similar to com.intellij.sh.run.ShRunFileAction
                // FIXME for some reason this doesn't work anymore https://github.com/artyushov/idea-jmh-plugin/issues/50
//                final AnAction[] actions = new AnAction[]{ActionManager.getInstance().getAction("RunClass")};
                AnAction[] actions = ExecutorAction.getActions(1);
                return new Info(ProfileYellow, actions, null);
            }
        }
        return null;
    }
}
