// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package ru.artyushov.jmhPlugin.configuration;

import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.application.JavaSettingsEditorBase;
import com.intellij.execution.ui.*;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiJavaModule;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.concurrency.NonUrgentExecutor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class JmhSettingsEditor extends JavaSettingsEditorBase<JmhConfiguration> {

  public JmhSettingsEditor(JmhConfiguration runConfiguration) {
    super(runConfiguration);
  }

  @Override
  protected void customizeFragments(List<SettingsEditorFragment<JmhConfiguration, ?>> fragments,
                                    SettingsEditorFragment<JmhConfiguration, ModuleClasspathCombo> moduleClasspath,
                                    CommonParameterFragments<JmhConfiguration> commonParameterFragments) {
    removeFragment(fragments, "runParallel");

    DefaultJreSelector jreSelector = DefaultJreSelector.fromModuleDependencies(moduleClasspath.component(), false);
    SettingsEditorFragment<JmhConfiguration, JrePathEditor> jrePath = CommonJavaFragments.createJrePath(jreSelector);
    fragments.add(jrePath);
    fragments.add(createShortenClasspath(moduleClasspath.component(), jrePath, false));
    fragments.add(commonParameterFragments.programArguments());
    if (!getProject().isDefault()) {
      SettingsEditorFragment<JmhConfiguration, TagButton> fragment =
        SettingsEditorFragment.createTag("test.use.module.path",
                                         ExecutionBundle.message("do.not.use.module.path.tag"),
                                         ExecutionBundle.message("group.java.options"),
                                         configuration -> !configuration.isUseModulePath(),
                                         (configuration, value) -> configuration.setUseModulePath(!value));
      fragments.add(fragment);
      ReadAction.nonBlocking(() -> {
        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(getProject());
        boolean noAnyModuleInfoFiles = FilenameIndex.getVirtualFilesByName(PsiJavaModule.MODULE_INFO_FILE, projectScope).isEmpty();
        fragment.setRemovable(noAnyModuleInfoFiles);
      }).expireWith(fragment).submit(NonUrgentExecutor.getInstance());
    }
  }

  @Override
  public boolean isInplaceValidationSupported() {
    return true;
  }

  private static void removeFragment(List<SettingsEditorFragment<JmhConfiguration, ?>> fragments, String fragmentId) {
    for (int i = 0; i < fragments.size(); i++) {
      if (fragments.get(i).getId().equals(fragmentId)) {
        fragments.remove(i);
        break;
      }
    }
  }

}
