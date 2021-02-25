/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.auntec.permissionsguide.thread;

/**
 * 线程执行对象  支持主线程和 其他线程  不必在new 线程 也不必线程池
 * @author shrek
 *
 */
public interface ZThreadEnforcer {

	void enforceMainThread(Runnable run);

	void enforceMainThreadDelay(Runnable run, long millisecond);
	
	void removeMainThread(Runnable run);
	
	void enforceBackgroud(Runnable run);
	
	void enforceBackgroudDelay(Runnable run, long millisecond);

	void removeBackgroud(Runnable run);
	
	void enforce(ThreadMode tMode, Runnable run);
	
	void enforceDelay(ThreadMode tMode, Runnable run, long millisecond);
	
}
