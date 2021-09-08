package io.getstream.chat.android.client.utils.observable

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.ChatEventListener
import io.getstream.chat.android.client.clientstate.DisconnectCause
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.events.ChannelDeletedEvent
import io.getstream.chat.android.client.events.ChannelHiddenEvent
import io.getstream.chat.android.client.events.ChannelTruncatedEvent
import io.getstream.chat.android.client.events.ChannelUpdatedByUserEvent
import io.getstream.chat.android.client.events.ChannelUpdatedEvent
import io.getstream.chat.android.client.events.ChannelUserBannedEvent
import io.getstream.chat.android.client.events.ChannelUserUnbannedEvent
import io.getstream.chat.android.client.events.ChannelVisibleEvent
import io.getstream.chat.android.client.events.ChatEvent
import io.getstream.chat.android.client.events.ConnectedEvent
import io.getstream.chat.android.client.events.ConnectingEvent
import io.getstream.chat.android.client.events.DisconnectedEvent
import io.getstream.chat.android.client.events.ErrorEvent
import io.getstream.chat.android.client.events.GlobalUserBannedEvent
import io.getstream.chat.android.client.events.GlobalUserUnbannedEvent
import io.getstream.chat.android.client.events.HealthEvent
import io.getstream.chat.android.client.events.MarkAllReadEvent
import io.getstream.chat.android.client.events.MemberAddedEvent
import io.getstream.chat.android.client.events.MemberRemovedEvent
import io.getstream.chat.android.client.events.MemberUpdatedEvent
import io.getstream.chat.android.client.events.MessageDeletedEvent
import io.getstream.chat.android.client.events.MessageReadEvent
import io.getstream.chat.android.client.events.MessageUpdatedEvent
import io.getstream.chat.android.client.events.NewMessageEvent
import io.getstream.chat.android.client.events.NotificationAddedToChannelEvent
import io.getstream.chat.android.client.events.NotificationChannelDeletedEvent
import io.getstream.chat.android.client.events.NotificationChannelMutesUpdatedEvent
import io.getstream.chat.android.client.events.NotificationChannelTruncatedEvent
import io.getstream.chat.android.client.events.NotificationInviteAcceptedEvent
import io.getstream.chat.android.client.events.NotificationInviteRejectedEvent
import io.getstream.chat.android.client.events.NotificationInvitedEvent
import io.getstream.chat.android.client.events.NotificationMarkReadEvent
import io.getstream.chat.android.client.events.NotificationMessageNewEvent
import io.getstream.chat.android.client.events.NotificationMutesUpdatedEvent
import io.getstream.chat.android.client.events.NotificationRemovedFromChannelEvent
import io.getstream.chat.android.client.events.ReactionDeletedEvent
import io.getstream.chat.android.client.events.ReactionNewEvent
import io.getstream.chat.android.client.events.ReactionUpdateEvent
import io.getstream.chat.android.client.events.TypingStartEvent
import io.getstream.chat.android.client.events.TypingStopEvent
import io.getstream.chat.android.client.events.UnknownEvent
import io.getstream.chat.android.client.events.UserDeletedEvent
import io.getstream.chat.android.client.events.UserPresenceChangedEvent
import io.getstream.chat.android.client.events.UserStartWatchingEvent
import io.getstream.chat.android.client.events.UserStopWatchingEvent
import io.getstream.chat.android.client.events.UserUpdatedEvent
import io.getstream.chat.android.client.logger.ChatLogger
import io.getstream.chat.android.client.models.EventType
import io.getstream.chat.android.client.socket.ChatSocket
import io.getstream.chat.android.client.socket.SocketListener
import java.util.Date

internal class ChatEventsObservable(
    private val socket: ChatSocket,
    private var client: ChatClient,
) {

    private var subscriptions = setOf<EventSubscription>()
    private var eventsMapper = EventsMapper(this)
    private val logger = ChatLogger.get("ChatEventsObservable")

    private fun onNext(event: ChatEvent) {
        subscriptions.forEach { subscription ->
            if (!subscription.isDisposed) {
                subscription.onNext(event)
            }
        }
        when (event) {
            is ConnectedEvent -> {
                client.callConnectionListener(event, null)
            }
            is ErrorEvent -> {
                client.callConnectionListener(null, event.error)
            }
            is ChannelDeletedEvent -> TODO()
            is ChannelHiddenEvent -> TODO()
            is ChannelTruncatedEvent -> TODO()
            is ChannelUpdatedByUserEvent -> TODO()
            is ChannelUpdatedEvent -> TODO()
            is ChannelUserBannedEvent -> TODO()
            is ChannelUserUnbannedEvent -> TODO()
            is ChannelVisibleEvent -> TODO()
            is MemberAddedEvent -> TODO()
            is MemberRemovedEvent -> TODO()
            is MemberUpdatedEvent -> TODO()
            is MessageDeletedEvent -> TODO()
            is MessageReadEvent -> TODO()
            is MessageUpdatedEvent -> TODO()
            is NewMessageEvent -> TODO()
            is NotificationAddedToChannelEvent -> TODO()
            is NotificationChannelDeletedEvent -> TODO()
            is NotificationChannelTruncatedEvent -> TODO()
            is NotificationInviteAcceptedEvent -> TODO()
            is NotificationInviteRejectedEvent -> TODO()
            is NotificationInvitedEvent -> TODO()
            is NotificationMarkReadEvent -> TODO()
            is NotificationMessageNewEvent -> TODO()
            is NotificationRemovedFromChannelEvent -> TODO()
            is ReactionDeletedEvent -> TODO()
            is ReactionNewEvent -> TODO()
            is ReactionUpdateEvent -> TODO()
            is TypingStartEvent -> TODO()
            is TypingStopEvent -> TODO()
            is UserStartWatchingEvent -> TODO()
            is UserStopWatchingEvent -> TODO()
            is ConnectingEvent -> TODO()
            is DisconnectedEvent -> TODO()
            is GlobalUserBannedEvent -> TODO()
            is GlobalUserUnbannedEvent -> TODO()
            is HealthEvent -> TODO()
            is MarkAllReadEvent -> TODO()
            is NotificationChannelMutesUpdatedEvent -> TODO()
            is NotificationMutesUpdatedEvent -> TODO()
            is UnknownEvent -> TODO()
            is UserDeletedEvent -> TODO()
            is UserPresenceChangedEvent -> TODO()
            is UserUpdatedEvent -> TODO()
        }
        subscriptions = subscriptions.filterNot(Disposable::isDisposed).toSet()
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if (subscriptions.isEmpty()) {
            socket.removeListener(eventsMapper)
        }
    }

    fun subscribe(
        filter: (ChatEvent) -> Boolean = { true },
        listener: ChatEventListener<ChatEvent>,
    ): Disposable {
        return addSubscription(SubscriptionImpl(filter, listener))
    }

    fun subscribeSingle(
        filter: (ChatEvent) -> Boolean = { true },
        listener: ChatEventListener<ChatEvent>,
    ): Disposable {
        return addSubscription(
            SubscriptionImpl(filter, listener).apply {
                afterEventDelivered = this::dispose
            }
        )
    }

    private fun addSubscription(subscription: EventSubscription): Disposable {
        if (subscriptions.isEmpty()) {
            // add listener to socket events only once
            socket.addListener(eventsMapper)
        }

        subscriptions = subscriptions + subscription

        return subscription
    }

    /**
     * Maps methods of [SocketListener] to events of [ChatEventsObservable]
     */
    private class EventsMapper(private val observable: ChatEventsObservable) : SocketListener() {

        override fun onConnecting() {
            observable.onNext(ConnectingEvent(EventType.CONNECTION_CONNECTING, Date()))
        }

        override fun onConnected(event: ConnectedEvent) {
            observable.onNext(event)
        }

        override fun onDisconnected(cause: DisconnectCause) {
            observable.onNext(DisconnectedEvent(EventType.CONNECTION_DISCONNECTED, Date(), cause))
        }

        override fun onEvent(event: ChatEvent) {
            observable.onNext(event)
        }

        override fun onError(error: ChatError) {
            observable.onNext(ErrorEvent(EventType.CONNECTION_ERROR, Date(), error))
        }
    }
}
