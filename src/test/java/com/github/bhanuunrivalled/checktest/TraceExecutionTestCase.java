package com.github.bhanuunrivalled.checktest;

import com.intellij.debugger.DebuggerTestCase;
import com.intellij.debugger.impl.OutputChecker;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebugSessionListener;
import com.intellij.xdebugger.frame.XStackFrame;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class TraceExecutionTestCase extends DebuggerTestCase {

    private final Logger LOG = Logger.getInstance(getClass());


    @Override
    protected OutputChecker initOutputChecker() {
        OutputChecker outputChecker = new OutputChecker(this::getTestAppPath, this::getAppOutputPath);
        return outputChecker;
    }

    // this does not work also
    @Override
    protected String getTestAppPath() {
        return new File("testData").getAbsolutePath();
    }
    // this does not work

    protected void doTest(boolean isResultNull) {
        try {
            final String className = getTestName(false);
            LOG.info("test name is + " + className);
            doTestImpl(isResultNull, className);
        } catch (Exception e) {
            throw new AssertionError("exception thrown", e);
        }
    }

    private void doTestImpl(boolean isResultNull, @NotNull String className)
            throws ExecutionException {
        LOG.info("Test started: " + getTestName(false));
        final AtomicBoolean completed = new AtomicBoolean(false);
        createLocalProcess(className);
        final XDebugSession session = getDebuggerSession().getXDebugSession();
        assertNotNull(session);

        session.addSessionListener(new XDebugSessionListener() {
            @Override
            public void sessionPaused() {
                if (completed.getAndSet(true)) {
                    LOG.info("session resuming ");
                    resume();
                    return;
                }
                try {
                    final XStackFrame currentStackFrame = session.getCurrentStackFrame();
                    Objects.requireNonNull(currentStackFrame, "Stack frame unexpectedly was null.");
                    LOG.info("session paused "  + currentStackFrame);
                    final TestCompositeNode nodeVisualizer = new TestCompositeNode();
                    // Happens in a different thread!

                    currentStackFrame.computeChildren(nodeVisualizer);
                    //sessionPausedImpl();
                    resume();
                } catch (Throwable t) {
                    println("Exception caught: " + t + ", " + t.getMessage(), ProcessOutputTypes.SYSTEM);
                    //noinspection CallToPrintStackTrace
                    t.printStackTrace();
                    resume();
                }
            }

            private void resume() {
                ApplicationManager.getApplication()
                        .invokeLater(session::resume);
            }
        }, getTestRootDisposable());
    }

}
