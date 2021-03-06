/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.pulsar

import java.{util => ju}

import org.apache.pulsar.client.admin.PulsarAdmin

object AdminUtils {

  import PulsarOptions._

  def buildAdmin(adminUrl: String, clientConf: ju.Map[String, Object]): PulsarAdmin = {
    val builder = PulsarAdmin.builder().serviceHttpUrl(adminUrl)
    val clientConfKeys = PulsarConfigurationUtils.clientConfKeys
    val caseSensitiveAuthPlugin = clientConfKeys.getOrElse(AUTH_PLUGIN_CLASS_NAME,"")
    if (clientConf.containsKey(caseSensitiveAuthPlugin)) {
      builder.authentication(
        clientConf.get(AUTH_PLUGIN_CLASS_NAME).toString, clientConf.get(AUTH_PARAMS).toString)
    }

    val caseSensitiveTLSInsecure = clientConfKeys.getOrElse(TLS_ALLOW_INSECURE_CONNECTION,"")
    if (clientConf.containsKey(caseSensitiveTLSInsecure)) {
      builder.allowTlsInsecureConnection(
        clientConf.get(TLS_ALLOW_INSECURE_CONNECTION).toString.toBoolean)
    }

    val caseSensitiveTLSVerification = clientConfKeys.getOrElse(TLS_HOSTNAME_VERIFICATION_ENABLE, "")
    if (clientConf.containsKey(caseSensitiveTLSVerification)) {
      builder.enableTlsHostnameVerification(
        clientConf.get(TLS_HOSTNAME_VERIFICATION_ENABLE).toString.toBoolean)
    }

    val caseSensitiveTrustPath = clientConfKeys.getOrElse(TLS_TRUST_CERTS_FILE_PATH, "")
    if (clientConf.containsKey(caseSensitiveTrustPath)) {
      builder.tlsTrustCertsFilePath(clientConf.get(TLS_TRUST_CERTS_FILE_PATH).toString)
    }

    builder.build()
  }

}
