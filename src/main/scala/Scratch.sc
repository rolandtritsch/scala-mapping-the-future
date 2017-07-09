import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.util.Success

val one: PartialFunction[Int, String] = { case 1 => "one" }
val two: PartialFunction[Int, String] = { case 2 => "two" }
val default: PartialFunction[Int, String] = { case _ => "!!!boom!!!" }
val partial = one.orElse(one).orElse(two).orElse(default)

one.isDefinedAt(1)
one.isDefinedAt(2)
one(1)
partial(2)
partial(3)

val list = (0 to 9).toList
val partialList = list.andThen { case e => e }
partialList.isDefinedAt(-1)
partialList.isDefinedAt(0)
partialList(9)

def inc(l: List[Int]): List[Int] = l.map(_ + 1)
def double(l: List[Int]): List[Int] = l.map(_ * 2)

val incDouble = (inc _).compose(double)
val doubleInc = (double _).compose(inc)
val incDouble2 = (double _).andThen(inc)
val doubleInc2 = (inc _).andThen(double)

val setOfList = Set(List(0, 1, 2), List(3, 4), List(9))

setOfList.map(e => e)
setOfList.flatMap(e => e)
//setOfList.compose(e => e)

setOfList.flatMap(incDouble(_))
setOfList.flatMap(doubleInc(_))
setOfList.flatMap(incDouble2(_))
setOfList.flatMap(doubleInc2(_))

val simpleIntPromise = Promise[Int]()
val simpleIntFuture = simpleIntPromise.future

simpleIntFuture.map(e => e)
// simpleIntFuture.flatMap(e => e)
simpleIntFuture.andThen { case Success(f) => f }

val optionIntPromise = Promise[Option[Int]]()
val optionIntFuture = optionIntPromise.future

optionIntFuture.map(e => e)
optionIntFuture.flatMap(e => Future(e.getOrElse(-7890)))