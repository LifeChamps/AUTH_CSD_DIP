package org.auth.csd.datalab.Utils

object Helpers {

  /**
   * Method to read environment variables
   *
   * @param key The key of the variable
   * @return The variable
   */
  @throws[NullPointerException]
  def readEnvVariable(key: String): String = {
    val envVariable = System.getenv(key)
    if (envVariable == null) throw new NullPointerException("Error! Environment variable " + key + " is missing")
    envVariable
  }

}
