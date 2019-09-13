/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 *
 * Licensed according to the LICENSE file in this repository.
 */

package org.whispersystems.signalservice.api.push;

import org.whispersystems.libsignal.util.guava.Optional;

import java.util.UUID;

/**
 * A class representing a message destination or origin.
 */
public class SignalServiceAddress {

  public static final int DEFAULT_DEVICE_ID = 1;

  private final Optional<UUID>   uuid;
  private final Optional<String> e164;
  private final Optional<String> relay;

  /**
   * Construct a PushAddress.
   *
   * @param uuid The UUID of the user, if available.
   * @param e164 The phone number of the user, if available.
   * @param relay The Signal service federated server this user is registered with (if not your own server).
   */
  public SignalServiceAddress(Optional<UUID> uuid, Optional<String> e164, Optional<String> relay) {
    this.uuid  = uuid;
    this.e164  = e164;
    this.relay = relay;
  }

  /**
   * Convenience constructor that will consider a UUID/E164 string absent if it is null or empty.
   */
  public SignalServiceAddress(UUID uuid, String e164) {
    this(Optional.fromNullable(uuid),
         e164 != null && !e164.isEmpty() ? Optional.of(e164) : Optional.<String>absent());
  }

  public SignalServiceAddress(Optional<UUID> uuid, Optional<String> e164) {
    this(uuid, e164, Optional.<String>absent());
  }

  public Optional<String> getNumber() {
    return e164;
  }

  public Optional<UUID> getUuid() {
    return uuid;
  }

  public String getIdentifier() {
    if (uuid.isPresent()) {
      return uuid.get().toString();
    } else if (e164.isPresent()) {
      return e164.get();
    } else {
      return null;
    }
  }

  public Optional<String> getRelay() {
    return relay;
  }

  public boolean matches(SignalServiceAddress other) {
    return (uuid.isPresent() && other.uuid.isPresent() && uuid.get().equals(other.uuid.get())) ||
           (e164.isPresent() && other.e164.isPresent() && e164.get().equals(other.e164.get()));
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof SignalServiceAddress)) return false;

    SignalServiceAddress that = (SignalServiceAddress)other;

    return equals(this.uuid, that.uuid) &&
           equals(this.e164, that.e164) &&
           equals(this.relay, that.relay);
  }

  @Override
  public int hashCode() {
    int hashCode = 0;

    if (this.uuid != null)      hashCode ^= this.uuid.hashCode();
    if (this.e164 != null)      hashCode ^= this.e164.hashCode();
    if (this.relay.isPresent()) hashCode ^= this.relay.get().hashCode();

    return hashCode;
  }

  private <T> boolean equals(Optional<T> one, Optional<T> two) {
    if (one.isPresent()) return two.isPresent() && one.get().equals(two.get());
    else                 return !two.isPresent();
  }
}
