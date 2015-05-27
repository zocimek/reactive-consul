package xebia.consul.client.dao

trait Indexed[T] {
  def index: Long
  def resource: T
}
