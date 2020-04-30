import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BuildingComponentsPage, BuildingDeleteDialog, BuildingUpdatePage } from './building.page-object';

const expect = chai.expect;

describe('Building e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let buildingComponentsPage: BuildingComponentsPage;
  let buildingUpdatePage: BuildingUpdatePage;
  let buildingDeleteDialog: BuildingDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Buildings', async () => {
    await navBarPage.goToEntity('building');
    buildingComponentsPage = new BuildingComponentsPage();
    await browser.wait(ec.visibilityOf(buildingComponentsPage.title), 5000);
    expect(await buildingComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.building.home.title');
    await browser.wait(ec.or(ec.visibilityOf(buildingComponentsPage.entities), ec.visibilityOf(buildingComponentsPage.noResult)), 1000);
  });

  it('should load create Building page', async () => {
    await buildingComponentsPage.clickOnCreateButton();
    buildingUpdatePage = new BuildingUpdatePage();
    expect(await buildingUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.building.home.createOrEditLabel');
    await buildingUpdatePage.cancel();
  });

  it('should create and save Buildings', async () => {
    const nbButtonsBeforeCreate = await buildingComponentsPage.countDeleteButtons();

    await buildingComponentsPage.clickOnCreateButton();

    await promise.all([
      buildingUpdatePage.setNameInput('name'),
      buildingUpdatePage.setPurchaseDateInput('2000-12-31'),
      buildingUpdatePage.setPropertyNumberInput('propertyNumber'),
      buildingUpdatePage.setStreetAddressInput('streetAddress'),
      buildingUpdatePage.setCityInput('city'),
      buildingUpdatePage.setStateCodeInput('stateCode'),
      buildingUpdatePage.setZipCodeInput('zipCode')
    ]);

    expect(await buildingUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await buildingUpdatePage.getPurchaseDateInput()).to.eq('2000-12-31', 'Expected purchaseDate value to be equals to 2000-12-31');
    expect(await buildingUpdatePage.getPropertyNumberInput()).to.eq(
      'propertyNumber',
      'Expected PropertyNumber value to be equals to propertyNumber'
    );
    expect(await buildingUpdatePage.getStreetAddressInput()).to.eq(
      'streetAddress',
      'Expected StreetAddress value to be equals to streetAddress'
    );
    expect(await buildingUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await buildingUpdatePage.getStateCodeInput()).to.eq('stateCode', 'Expected StateCode value to be equals to stateCode');
    expect(await buildingUpdatePage.getZipCodeInput()).to.eq('zipCode', 'Expected ZipCode value to be equals to zipCode');

    await buildingUpdatePage.save();
    expect(await buildingUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await buildingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Building', async () => {
    const nbButtonsBeforeDelete = await buildingComponentsPage.countDeleteButtons();
    await buildingComponentsPage.clickOnLastDeleteButton();

    buildingDeleteDialog = new BuildingDeleteDialog();
    expect(await buildingDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.building.delete.question');
    await buildingDeleteDialog.clickOnConfirmButton();

    expect(await buildingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
