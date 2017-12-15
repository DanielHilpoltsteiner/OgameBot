package ogamebot.online;

import java.io.IOException;

/**
 *
 */
interface OnlineSupplier<T> {
    Void update(AccountAccess updater, T t) throws IOException;

    T create(AccountAccess updater) throws IOException;
}
