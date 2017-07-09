package mapping.the.future

import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Promise
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Futures extends StrictLogging {

  def mapASimpleFuture(f: Future[Int]): Future[Int] = f.map(i => {
    logger.debug(s"mapping: ${i}")
    i
  })

  def andThenASimpleFuture(f: Future[Int]): Future[Int] = f.andThen {
    case Success(i) => logger.debug(s"andthen: ${i}")
    case Failure(e) => logger.debug(s"andthen (fail): ${e.getMessage}")
  }

  def mapAnOptionFuture(f: Future[Option[Int]]): Future[Int] = f.flatMap(i => {
    val f = i.getOrElse(-7890)
    logger.debug(s"flat mapping: ${f}")
    Future(f)
  })

  def main(args: Array[String]): Unit = {
    require(args.size == 0, "Usage: mapping.the.future.Futures")

    val simpleIntPromise = Promise[Int]()
    val simpleIntFuture = simpleIntPromise.future

    val anotherSimpleIntFuture = mapASimpleFuture(simpleIntFuture)
    logger.debug(s"before the promise: ${simpleIntFuture}")
    simpleIntPromise.success(1234)
    logger.debug(s"after the promise: ${simpleIntFuture}")
    // why is that not a success too?
    logger.debug(s"after the (another) promise: ${anotherSimpleIntFuture}")

    val simpleIntPromise2 = Promise[Int]()
    val simpleIntFuture2 = simpleIntPromise2.future

    val anotherSimpleIntFuture2 = andThenASimpleFuture(simpleIntFuture2)
    logger.debug(s"before the promise: ${simpleIntFuture2}")
    simpleIntPromise2.success(4321)
    //simpleIntPromise2.failure(new Exception("boom"))
    logger.debug(s"after the promise: ${simpleIntFuture2}")
    // why is that not a success too?
    logger.debug(s"after the (another) promise: ${anotherSimpleIntFuture2}")

    val optionIntPromise = Promise[Option[Int]]()
    val optionIntFuture = optionIntPromise.future

    val anotherOptionIntFuture = mapAnOptionFuture(optionIntFuture)
    logger.debug(s"before the option promise: ${optionIntFuture}")
    optionIntPromise.success(Some(5678))
    logger.debug(s"after the option promise: ${optionIntFuture}")
    // why is that not a success too?
    logger.debug(s"after the (another) option promise: ${anotherOptionIntFuture}")

    //val iii = f.andThen(i => i)
  }
}