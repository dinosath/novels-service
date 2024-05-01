import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/novel">
        <Translate contentKey="global.menu.entities.novel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/genre">
        <Translate contentKey="global.menu.entities.genre" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tag">
        <Translate contentKey="global.menu.entities.tag" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/author">
        <Translate contentKey="global.menu.entities.author" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chapter">
        <Translate contentKey="global.menu.entities.chapter" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
