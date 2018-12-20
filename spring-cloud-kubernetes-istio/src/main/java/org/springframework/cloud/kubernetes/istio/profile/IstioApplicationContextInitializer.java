/*
 *     Copyright (C) 2016 to the original authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.springframework.cloud.kubernetes.istio.profile;

import me.snowdrop.istio.client.DefaultIstioClient;
import org.springframework.cloud.kubernetes.istio.utils.MeshUtils;
import org.springframework.cloud.kubernetes.istio.utils.StandardMeshUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

public class IstioApplicationContextInitializer implements
	ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

	private static final int ORDER = 100;
	private final IstioProfileApplicationListener listener;

	public IstioApplicationContextInitializer() {
		//If we are inside Kubernetes this should be perfectly valid.
		//If not then we won't add the Kubernetes profile anyway.
		this(new StandardMeshUtils(new DefaultIstioClient()));
	}

	public IstioApplicationContextInitializer(MeshUtils utils) {
		this(new IstioProfileApplicationListener(utils));
	}

	public IstioApplicationContextInitializer(IstioProfileApplicationListener listener) {
		this.listener = listener;
	}

	@Override
	public int getOrder() {
		return ORDER;
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		listener.addKubernetesProfile(applicationContext.getEnvironment());
	}
}
