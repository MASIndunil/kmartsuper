package lk.kmartsuper.asset.userManagement.dao;

import lk.kmartsuper.asset.userManagement.entity.Enum.UserSessionLogStatus;
import lk.kmartsuper.asset.userManagement.entity.User;
import lk.kmartsuper.asset.userManagement.entity.UserSessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionLogDao extends JpaRepository<UserSessionLog, Integer > {
    UserSessionLog findByUserAndUserSessionLogStatus(User user, UserSessionLogStatus userSessionLogStatus);
}
