package org.ergoplatform.common.sttp

import cats.Monad
import cats.syntax.either._
import org.ergoplatform.ergo.errors.ResponseError
import sttp.client3.{Response, ResponseException}
import tofu.Throws
import tofu.syntax.monadic._
import tofu.syntax.raise._

object syntax {

  implicit class ResponseOps[F[_], A](private val fr: F[Response[Either[ResponseException[String, io.circe.Error], A]]])
    extends AnyVal {

    def absorbError(implicit R: Throws[F], A: Monad[F]): F[A] =
      fr.flatMap(_.body.leftMap(resEx => ResponseError(resEx.getMessage)).toRaise)
  }
}
