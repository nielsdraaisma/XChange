package org.knowm.xchange.omf;

import org.knowm.xchange.dto.Order;

public enum OMFOrderFlags implements Order.IOrderFlags {
  VOLUME_IN_COUNTER_CURRENCY,
  SKIP_ACCEPT_SPOT_REQUEST
}
