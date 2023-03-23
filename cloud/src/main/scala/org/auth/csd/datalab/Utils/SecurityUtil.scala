package org.auth.csd.datalab.Utils

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMReader

import java.io.{ByteArrayInputStream, InputStreamReader}
import java.nio.file.{Files, Paths}
import java.security.{KeyStore, Security}
import java.security.cert.{CertificateException, X509Certificate}
import javax.net.ssl.{KeyManagerFactory, SSLContext, SSLSocketFactory, TrustManager, X509TrustManager}
import scala.io.Source

object SecurityUtil {
  @throws[Exception]
  def getSocketFactory(crtFile: String): SSLSocketFactory = {
    Security.addProvider(new BouncyCastleProvider())
    val resource = Source.fromResource(crtFile)
    val reader_client = new PEMReader((new InputStreamReader(new ByteArrayInputStream(resource.mkString.getBytes))))
    //    val resource = getClass.getClassLoader.getResource(crtFile)
    //    val reader_client = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(resource.getPath)))))
    //val reader_client = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))))
    val cert = reader_client.readObject().asInstanceOf[X509Certificate]
    reader_client.close()
    val ks = KeyStore.getInstance(KeyStore.getDefaultType)
    ks.load(null, null)
    ks.setCertificateEntry("lc-prod-cert", cert) //TODO: modify that to the name of the cert
    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    kmf.init(ks, "".toCharArray)
    val context = SSLContext.getInstance("TLSv1.2")
    val tms = new Array[TrustManager](1)
    val miTM = new SecurityUtil.TrustAllManager
    tms(0) = miTM
    context.init(kmf.getKeyManagers, tms, null)
    reader_client.close()
    context.getSocketFactory
  }

  class TrustAllManager extends TrustManager with X509TrustManager {
    override def getAcceptedIssuers: Array[X509Certificate] = null

    @throws[CertificateException]
    def checkServerTrusted(certs: Array[X509Certificate], authType: String): Unit = {
    }

    def isServerTrusted(certs: Array[X509Certificate]) = true

    def isClientTrusted(certs: Array[X509Certificate]) = true

    @throws[CertificateException]
    def checkClientTrusted(certs: Array[X509Certificate], authType: String): Unit = {
    }
  }
}
